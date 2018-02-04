package com.sooraj.transaction.statistics.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.sooraj.transaction.statistics.exception.OutOfTimeRangeException;
import com.sooraj.transaction.statistics.model.Statistic;
import com.sooraj.transaction.statistics.service.StatisticService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)
@WebMvcTest(value = StatisticController.class, secure = false)
public class StatisticControllerTest {
	
	@Autowired
    private MockMvc mvc;

    @MockBean
    private StatisticService statisticService;


    @Test
    public void testForCorrectStat() throws Exception {
        when(statisticService.getStatistics()).thenReturn(Statistic.builder()
                .count(10)
                .sum(155.9)
                .max(47.5)
                .min(11.2)
                .build());

        mvc.perform(get("/statistics").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("count", is(10)))
                .andExpect(jsonPath("sum", is(155.9)))
                .andExpect(jsonPath("avg", is(15.59)))
                .andExpect(jsonPath("max", is(47.5)))
                .andExpect(jsonPath("min", is(11.2)));
    }

    @Test
    public void testForNoContentStatus() throws Exception {
        when(statisticService.getStatistics()).thenThrow(new OutOfTimeRangeException("Invalid TIme"));

        mvc.perform(get("/statistics").accept("application/json"))
                .andExpect(status().isNoContent());
   }

}
