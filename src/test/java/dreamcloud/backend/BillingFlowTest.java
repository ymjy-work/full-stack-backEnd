package dreamcloud.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BillingFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void consumeKeepsQuotaAndLedgerConsistent() throws Exception {
        String token = register("flow_user");
        subscribe(token, 2L);

        for (int i = 0; i < 10; i++) {
            assertEquals(200, consume(token).get("code").asInt());
        }

        Long userId = userId("flow_user");
        assertEquals(100, quotaUsed(userId), "quota_used 应等于 10 次 × 10");
        assertEquals(10, usageCount(userId), "流水条数应等于消耗次数");
        assertEquals(100, usageSum(userId), "流水金额之和应与已用额度一致");
        assertEquals(1900, jdbcTemplate.queryForObject(
                        "select quota_remaining_after from usage_record where user_id = ? order by id desc limit 1",
                        Integer.class, userId),
                "流水里的剩余额度应与周期一致");

        JsonNode quota = quota(token);
        assertEquals(2000, quota.get("quotaLimit").asInt());
        assertEquals(100, quota.get("quotaUsed").asInt());
        assertEquals(1900, quota.get("quotaRemaining").asInt());
        assertTrue(quota.get("periodEnd").asString()
                        .matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"),
                "JSON 时间应为 yyyy-MM-dd HH:mm:ss，实际: " + quota.get("periodEnd").asString());

        String stored = jdbcTemplate.queryForObject(
                "select period_end from quota_period where user_id = ?", String.class, userId);
        assertTrue(stored.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"),
                "数据库时间应为 yyyy-MM-dd HH:mm:ss，实际: " + stored);
    }

    @Test
    void secondSubscribeFailsWhenBalanceInsufficient() throws Exception {
        String token = register("balance_user");
        subscribe(token, 2L);

        JsonNode body = objectMapper.readTree(mockMvc.perform(post("/billing/subscribe")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"planId\":2}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(40001, body.get("code").asInt());
    }

    @Test
    void consumeFailsWhenQuotaExhaustedAndWritesNoRecord() throws Exception {
        String token = register("quota_user");
        subscribe(token, 2L);
        Long userId = userId("quota_user");

        jdbcTemplate.update("update quota_period set quota_used = 1990 where user_id = ?", userId);

        assertEquals(200, consume(token).get("code").asInt());
        assertEquals(2000, quotaUsed(userId));

        JsonNode body = objectMapper.readTree(mockMvc.perform(post("/billing/consume")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"action\":\"start_game\"}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));

        assertEquals(40002, body.get("code").asInt());
        assertEquals(1, usageCount(userId), "超额请求不应写入流水");
    }

    private String register(String username) throws Exception {
        String body = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode json = objectMapper.readTree(body);
        assertEquals(200, json.get("code").asInt(), "注册失败: " + body);
        return json.get("data").get("token").asString();
    }

    private void subscribe(String token, Long planId) throws Exception {
        String body = mockMvc.perform(post("/billing/subscribe")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"planId\":" + planId + "}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals(200, objectMapper.readTree(body).get("code").asInt(), "订阅失败: " + body);
    }

    private JsonNode consume(String token) throws Exception {
        String body = mockMvc.perform(post("/billing/consume")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"action\":\"start_game\"}"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readTree(body);
    }

    private JsonNode quota(String token) throws Exception {
        String body = mockMvc.perform(get("/billing/quota")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readTree(body).get("data");
    }

    private Long userId(String username) {
        return jdbcTemplate.queryForObject(
                "select id from users where username = ?", Long.class, username);
    }

    private Integer quotaUsed(Long userId) {
        return jdbcTemplate.queryForObject(
                "select quota_used from quota_period where user_id = ?", Integer.class, userId);
    }

    private Integer usageCount(Long userId) {
        return jdbcTemplate.queryForObject(
                "select count(*) from usage_record where user_id = ?", Integer.class, userId);
    }

    private Integer usageSum(Long userId) {
        return jdbcTemplate.queryForObject(
                "select coalesce(sum(amount), 0) from usage_record where user_id = ?", Integer.class, userId);
    }
}
