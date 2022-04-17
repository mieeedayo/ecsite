package jp.co.internous.ecsite.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.ecsite.model.dao.GoodsRepository;
import jp.co.internous.ecsite.model.dao.PurchaseRepository;
import jp.co.internous.ecsite.model.dao.UserRepository;
import jp.co.internous.ecsite.model.dto.HistoryDto;
import jp.co.internous.ecsite.model.dto.LoginDto;
import jp.co.internous.ecsite.model.entity.Goods;
import jp.co.internous.ecsite.model.entity.Purchase;
import jp.co.internous.ecsite.model.entity.User;
import jp.co.internous.ecsite.model.form.CartForm;
import jp.co.internous.ecsite.model.form.GoodsForm;
import jp.co.internous.ecsite.model.form.HistoryForm;
import jp.co.internous.ecsite.model.form.LoginForm;

@Controller
@RequestMapping("/ecsite")
public class IndexController {

	@Autowired
	private UserRepository userRepos;
	@Autowired
	private GoodsRepository goodsRepos;
	@Autowired
	private PurchaseRepository purchaseRepos;
	
	private Gson gson=new Gson();

	@RequestMapping("/")
	public String index(Model m) {
		List<Goods> goods= goodsRepos.findAll();
		m.addAttribute("goods", goods);
		return "index";
	}
	
	@ResponseBody
	@PostMapping("/api/login")
	public String loginApi(@RequestBody LoginForm form) {
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());
LoginDto dto= new LoginDto(0,null,null,"ゲスト");
		if(users.size() >0) {
			dto = new LoginDto(users.get(0));
		}
		return gson.toJson(dto);
	}
	
	@ResponseBody
	@PostMapping("/api/purchase")
	public String purchaseApi(@RequestBody CartForm f) {
		f.getCartList().forEach((c)->{
			long total =c.getPrice()*c.getCount();
			purchaseRepos.persist(f.getUserId(),c.getId(),c.getGoodsName(),c.getCount(),total);
			
		});
		return String.valueOf(f.getCartList().size());
	}
	@ResponseBody
	@PostMapping("/api/history")
	public String historyApi(@RequestBody HistoryForm form) {
	String userId =form.getUserId();
	List<Purchase> history = purchaseRepos.findHistory(Long.parseLong(userId));
	List<HistoryDto> historyDtoList = new ArrayList<>();
	history.forEach((v)->{
		HistoryDto dto=new HistoryDto(v, null);
		historyDtoList.add(dto);
	});
	return gson.toJson(historyDtoList);
	}
	/*

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
*/
}
