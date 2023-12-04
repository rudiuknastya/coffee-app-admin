package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.entity.OrderStatus;

@Controller
public class StatisticController {
    @GetMapping("/statistic")
    public String statistic(Model model){
        model.addAttribute("pageNum", 1);
        return "statistic/statistic";
    }
}
