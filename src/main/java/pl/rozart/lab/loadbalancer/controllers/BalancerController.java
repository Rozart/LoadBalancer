package pl.rozart.lab.loadbalancer.controllers;

import org.apache.ignite.resources.LoadBalancerResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BalancerController {

    @LoadBalancerResource
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, Model model) {
        String sessionId = request.getSession(true).getId();
        model.addAttribute("sessionId", sessionId);
        return "index";
    }

    @LoadBalancerResource
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, Model model) {
        request.getSession().invalidate();
        String sessionId = request.getSession().getId();
        model.addAttribute("sessionId", sessionId);
        return "index";
    }

}
