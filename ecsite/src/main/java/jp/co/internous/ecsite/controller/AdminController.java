package jp.co.internous.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.internous.ecsite.model.dao.GoodsRepository;
import jp.co.internous.ecsite.model.dao.UserRepository;
import jp.co.internous.ecsite.model.dto.LoginDto;
import jp.co.internous.ecsite.model.entity.Goods;
import jp.co.internous.ecsite.model.entity.User;
import jp.co.internous.ecsite.model.form.GoodsForm;
import jp.co.internous.ecsite.model.form.LoginForm;
import jp.co.internous.ecsite.model.form.UserForm;

@Controller
@RequestMapping("/ecsite/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepos;
	@Autowired
	private GoodsRepository goodsRepos;

	@RequestMapping("/")
	public String index() {
		return "adminindex";
	}

	@PostMapping("/welcome")
	public String welcome(LoginForm form, Model m) {
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		if (users != null && users.size() > 0) {
			boolean isAdmin = users.get(0).getIsAdmin() != 0;
			if (isAdmin) {
				List<Goods> goods = goodsRepos.findAll();
				m.addAttribute("userName", users.get(0).getUserName());
				m.addAttribute("password", users.get(0).getPassword());
				m.addAttribute("goods", goods);
			}
		}
		return "welcome";
	}

	@RequestMapping("/goodsMst")
	public String goodsMst(LoginForm form, Model m) {
		m.addAttribute("userName", form.getUserName());
		m.addAttribute("password", form.getPassword());
		return "goodsmst";
	}

	@RequestMapping("/addGoods")
	public String addGoods(GoodsForm goodsForm, LoginForm loginform, Model m) {
		m.addAttribute("userName", loginform.getUserName());
		m.addAttribute("password", loginform.getPassword());

		Goods goods = new Goods();
		goods.setGoodsName(goodsForm.getGoodsName());
		goods.setPrice(goodsForm.getPrice());
		goodsRepos.saveAndFlush(goods);
		return "forward:/ecsite/admin/welcome";
	}

	@ResponseBody
	@PostMapping("/api/deleteGoods")
	public String deleteApi(@RequestBody GoodsForm f, Model m) {
		try {
			goodsRepos.deleteById(f.getId());
		} catch (IllegalArgumentException e) {
			return "-1";
		}
		return "1";
	}

	@RequestMapping("/usermst")
	public String userMst(LoginForm form, Model m) {
		m.addAttribute("userName", form.getUserName());
		m.addAttribute("password", form.getPassword());
//		m.addAttribute("fullName",form.getFullName());
		return "usermst";
	}

	@RequestMapping("/addUser")
	public String addUser(UserForm userForm, Model m) {
		m.addAttribute("userName", userForm.getUserName());
		m.addAttribute("password", userForm.getPassword());
		m.addAttribute("fullName", userForm.getFullName());

		User user = new User();
		user.setUserName(userForm.getUserName());
		user.setPassword(userForm.getPassword());
		user.setFullName(userForm.getFullName());
		userRepos.saveAndFlush(user);
		return "adminindex";
	}

@ResponseBody
	@PostMapping("api/check")
	public String check(@RequestBody LoginForm form) {
		String flag="-1";
		List<User> users = userRepos.findByUserName(form.getUserName());
		if (users != null && users.size() > 0) {
//			リターンで結果を返したい。
			flag="1";
		}else {
			flag="0";
		}
			return flag;
}}

/*
 * Model m
 * 
 * @PostMapping("check") public String check(@RequestParam String userName,Model
 * model) {
 * 
 * List<User> users = userRepos.findByUserName(users.findByUserName());
 * users.findByUserName(); return users; }catch(Exception e){ return null;} if
 * (users != null) { String msg ="重複してるよ"; model.addAttribute("msg",msg); return
 * "check"; }else { String msg2 ="重複してない"; model.addAttribute("msg2",msg2);
 * return "check"; }
 * 
 * }
 * 
 * 
 */
