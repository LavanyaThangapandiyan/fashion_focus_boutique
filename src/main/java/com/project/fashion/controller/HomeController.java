package com.project.fashion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.project.fashion.service.UserService;

@Controller
public class HomeController {
	UserService userService = new UserService();

	@GetMapping("/filter")
	public String getChiffonSarees() {
		return "filter";
	}

	@GetMapping("/chiffon")
	public String showChiffonSarees(Model model) {
		String name = "Chiffon Sarees";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/silksaree")
	public String showSilkSaree(Model model) {
		String name = "Silk Sarees";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/rayonkurti")
	public String showAllKurti(Model model) {
		String name = "Rayon Kurti";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/allkurtas")
	public String showAllKurtaSets(Model model) {
		String name = "Kurta Sets";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/duppata")
	public String showDuppatas(Model model) {
		String name = "Duppatas";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/cottonsaree")
	public String showCottonSaree(Model model) {
		String name = "Cotton Sarees";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/embroidery")
	public String showEmbroideryKurti(Model model) {
		String name = "Embroidery Kurtis";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/lehanga")
	public String showLehangas(Model model) {
		String name = "Lehanga";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/georgette")
	public String getGeorgetteSaree(Model model) {
		String name = "Georgette Sarees";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/cottonkurti")
	public String showCottonKurti(Model model) {
		String name = "Cotton Kurtis";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/gowns")
	public String showGownList(Model model) {
		String name = "Gown";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/tops")
	public String showTops(Model model) {
		String name = "Western";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/jeans")
	public String showJeansPage(Model model) {
		String name = "Jeans";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/nightsuits")
	public String showNightSuitsList(Model model) {
		String name = "Night Suits";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/jeggings")
	public String showjeggins(Model model) {
		String name = "Jeggings";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/plazzo")
	public String showAllPlazzoList(Model model) {
		String name = "Plazzos";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/sweaters")
	public String showSweatersList(Model model) {
		String name = "Sweaters";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/jumpsuits")
	public String showJumpSuits(Model model) {
		String name = "Jump Suits";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/anarkali")
	public String showAllsarees(Model model) {
		String name = "Anarkali Kurti";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/topwear")
	public String showAllMenTop(Model model) {
		String name = "Men Top Wear";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/mbottom")
	public String showAllBottom(Model model) {
		String name = "Men Bottom wear";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}

	@GetMapping("/kids")
	public String showAllKids(Model model) {
		String name = "Kidz";
		model.addAttribute("filterlist", userService.allProductList(name));
		return "filter";
	}
}
