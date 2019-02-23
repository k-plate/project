package com.doug.kplate.controller.user;

import com.doug.kplate.common.util.Query;
import com.doug.kplate.dto.user.AgentDto;
import com.doug.kplate.entity.user.Agent;
import com.doug.kplate.service.user.AgentService;
import com.doug.kplate.utils.PageUtils;
import com.doug.kplate.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @RequestMapping("/list")
    public R List(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        Map<String, Object> param = new HashMap<>();

        List<AgentDto> agentDtos = agentService.queryAll(query);
        Integer total = agentService.queryCount(query);

        PageUtils pageUtil = new PageUtils(agentDtos, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }
}
