package io.github.deepakjvm.omnigrocer.seed;

import lombok.Getter;

@Getter
public class ProductJson {
  private String name;
  private String image;
  private String category;
	private int quantity;
	private int price;
	private int weight;
	private String description;
}
