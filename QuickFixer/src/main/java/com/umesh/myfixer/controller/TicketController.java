package com.umesh.myfixer.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.umesh.myfixer.entity.Ticket;
import com.umesh.myfixer.entity.User;
import com.umesh.myfixer.services.TicketService;
import com.umesh.myfixer.services.UserService;

@Controller
public class TicketController {

	@Autowired
	TicketService ticketService;

	@Autowired
	UserService userService;

	// PUBLIC_INTERFACE
	@GetMapping(value = "/webrst/ticketForm")
	/**
	 * Render ticket create/edit form.
	 *
	 * @param ticketId optional ticket id to edit.
	 * @return ModelAndView for the post-login content page with the ticket form displayed.
	 */
	public ModelAndView showTicketForm(@RequestParam(required = false) Integer ticketId) {
		int userId = this.getLoggedUser();
		ModelAndView mav = new ModelAndView("postLoginContent");
		Ticket ticket = null;
		if (ticketId != null) {
			ticket = ticketService.getTicketByTicketid(ticketId);
			mav.addObject("ticket", ticket);
		} else {
			mav.addObject("userId", userId);
			mav.addObject("ticket", new Ticket());
		}
		mav.addObject("showTicketForm", true);
		return mav;
	}

	// PUBLIC_INTERFACE
	@PostMapping("/webrst/saveTicketData")
	/**
	 * Create or update a ticket (state-changing action). CSRF token is required.
	 *
	 * @param ticket ticket model bound from form submission.
	 * @return redirect to the ticket list page.
	 */
	public String saveTicket(@ModelAttribute("ticket") Ticket ticket) {
		if (ticket.getId() == null) {
			ticket.setCreatedDate(new java.sql.Timestamp(new Date().getTime()));
		}
		ticketService.save(ticket);
		return "redirect:ticket";
	}

	// PUBLIC_INTERFACE
	@GetMapping(value = "/webrst/editTicket")
	/**
	 * Render ticket edit page for a given ticket id (read-only render).
	 *
	 * @param id ticket id.
	 * @return ModelAndView for the post-login content page with the ticket form displayed.
	 */
	public ModelAndView editTicket(@RequestParam("id") int id) {
		ModelAndView mav = new ModelAndView("postLoginContent");
		mav.addObject("ticket", ticketService.get(id));
		mav.addObject("showTicketForm", true);
		mav.addObject("readOnly", true);
		return mav;
	}

	// PUBLIC_INTERFACE
	@PostMapping(value = "/webrst/deleteTicket")
	/**
	 * Delete a ticket (state-changing action). Uses POST to support classic JSP forms.
	 * CSRF token is required.
	 *
	 * @param id ticket id.
	 * @return redirect to the ticket list page.
	 */
	public String deleteTicket(@RequestParam("id") int id) {
		ticketService.delete(id);
		return "redirect:ticket";
	}

	private int getLoggedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		User user = userService.findUserByName(username);
		return user.getId();
	}
}
