package jp.co.internous.ecsite.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.internous.ecsite.model.entity.Goods;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
