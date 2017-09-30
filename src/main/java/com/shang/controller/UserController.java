package com.shang.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shang.domain.User;
import com.shang.service.UserService;

/**
 * 功能概要：UserController
 * 
 * @author shanghaitao
 * @since  2017年8月22日 
 */
@Controller
public class UserController {
	@Resource
	private UserService userService;
	
	@RequestMapping("/")  
    public ModelAndView getIndex(){    
		System.out.println("1111");
		ModelAndView mav = new ModelAndView("index"); 
		User user = userService.selectUserById(1);
		System.out.println("2222");
	    mav.addObject("user", user); 
	    System.out.println(mav.toString());
        return mav;  
    }  
}
