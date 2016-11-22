package exterminatorjeff.undergroundbiomes.common.block.slab;

/**
 * 
 * @author LouisDB
 *
 */
public class UBSlabPair {

	public final UBStoneSlab half;
	public final UBStoneSlab full;

	public UBSlabPair(UBStoneSlab half, UBStoneSlab full) {
		assert !half.isDouble() && full.isDouble();
		this.half = half;
		this.full = full;
	}

	public UBStoneSlab getOther(UBStoneSlab slab) {
		return slab.isDouble() ? half : full;
	}

}
