package org.minnie.utility.module.combie;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用排列组合算法工具类
 * @author eazytec
 *
 */
public class ArrangeCombine {

	public static List<String> getArrangeOrCombine(String[] args, int n,
			boolean isArrange) throws Exception {
		if (args.length <= 0) {
			throw new Exception("array.length<=0");
		}
		if (n > args.length) {
			throw new Exception(" n>array.length");
		}
		Combination comb = new Combination();
		comb.mn(args, n);
		if (!isArrange) {
			return comb.getCombList();
		}
		List<String> arrangeList = new ArrayList<String>();
		for (int i = 0; i < comb.getCombList().size(); i++) {
			String[] list = comb.getCombList().get(i).split(",");
			Arrange ts = new Arrange();
			ts.perm(list, 0, list.length - 1);
			for (int j = 0; j < ts.getArrangeList().size(); j++) {
				arrangeList.add(ts.getArrangeList().get(j));
			}
		}
		return arrangeList;
	}

	public static void main(String[] args) {
		try {
			List<String> arrangeOrCombine = ArrangeCombine.getArrangeOrCombine(
					new String[] { "1", "2", "3", "4", "5", "6" }, 3, false);
			List<String> arrangeOrCombine2 = ArrangeCombine
					.getArrangeOrCombine(new String[] { "1", "2", "3", "4",
							"5", "6" }, 3, true);
			for (int i = 0; i < arrangeOrCombine.size(); i++) {
				System.out.println(arrangeOrCombine.get(i));
			}
			System.out.println("Total:" + arrangeOrCombine.size());
			System.out.println("=============================");
			for (int i = 0; i < arrangeOrCombine2.size(); i++) {
				System.out.println(arrangeOrCombine2.get(i));
			}
			System.out.println("Total:" + arrangeOrCombine2.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}