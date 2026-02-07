package com.umesh.myfixer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.umesh.myfixer.entity.Authorities;
import com.umesh.myfixer.entity.User;
import com.umesh.myfixer.services.AuthoritiesService;
import com.umesh.myfixer.services.UserService;
import com.umesh.myfixer.utils.PasswordEncode;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AuthoritiesService authorityService;

	// PUBLIC_INTERFACE
	@GetMapping(value = "/webrst/userForm")
	/**
	 * Render user create form.
	 *
	 * @return ModelAndView for the post-login content page with the user form displayed.
	 */
	public ModelAndView showUserForm() {
		ModelAndView mav = new ModelAndView("postLoginContent");
		mav.addObject("showUserForm", true);
		mav.addObject("user", new User());
		return mav;
	}

	// PUBLIC_INTERFACE
	@PostMapping(value = "/webrst/saveUserData")
	/**
	 * Create or update a user (state-changing action). CSRF token is required.
	 *
	 * @param user user model bound from form submission.
	 * @return redirect to the user list page.
	 */
	public String saveUser(@ModelAttribute("user") User user) {
		userService.save(user);
		return "redirect:user";
	}

	// PUBLIC_INTERFACE
	@GetMapping(value = "/webrst/editUser")
	/**
	 * Render user edit form (read-only render).
	 *
	 * @param id user id.
	 * @return ModelAndView for the post-login content page with the user form displayed.
	 */
	public ModelAndView editUser(@RequestParam("id") int id) {
		ModelAndView mav = new ModelAndView("postLoginContent");
		User user = userService.get(id);
		mav.addObject("user", user);
		mav.addObject("showUserForm", true);
		return mav;
	}

	// PUBLIC_INTERFACE
	@PostMapping(value = "/webrst/deleteUser")
	/**
	 * Delete a user (state-changing action). Uses POST to support classic JSP forms.
	 * CSRF token is required.
	 *
	 * @param id user id.
	 * @return redirect to the user list page.
	 */
	public String deleteUser(@RequestParam("id") int id) {
		userService.delete(id);
		return "redirect:user";
	}

	// PUBLIC_INTERFACE
	@ModelAttribute("role")
	/**
	 * Provides the list of authorities/roles for form select fields.
	 *
	 * @return list of Authorities.
	 */
	public List<Authorities> list() {
		return authorityService.list();
	}

	// PUBLIC_INTERFACE
	@PostMapping(value = "/webrst/change/changePassword")
	/**
	 * Change password for current authenticated user (state-changing action). CSRF token is required.
	 *
	 * @param request HttpServletRequest
	 * @param oldPassword old password from form
	 * @param newPassword new password from form
	 * @param response HttpServletResponse
	 * @return redirect to /home
	 */
	public String getUser(HttpServletRequest request, String oldPassword, String newPassword, HttpServletResponse response) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findUserByName(authentication.getName());
			if (PasswordEncode.checkPassword(oldPassword, user.getPassword())) {
				userService.changePassword(newPassword, user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/home";
	}

}
