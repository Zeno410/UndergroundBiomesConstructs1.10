package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.common.UBStonesRegistry;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneType;
import exterminatorjeff.undergroundbiomes.api.names.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LouisDB
 */
public enum StonesRegistry implements UBStonesRegistry {
  INSTANCE;

  private StoneEntry getStoneEntry(UBStoneType type, UBStoneStyle style) {
    switch (type) {
      case IGNEOUS:
        switch (style) {
          case STONE:
            return API.IGNEOUS_STONE;
          case COBBLE:
            return API.IGNEOUS_COBBLE;
          case BRICK:
            return API.IGNEOUS_BRICK;
        }
        break;
      case METAMORPHIC:
        switch (style) {
          case STONE:
            return API.METAMORPHIC_STONE;
          case COBBLE:
            return API.METAMORPHIC_COBBLE;
          case BRICK:
            return API.METAMORPHIC_BRICK;
        }
        break;
      case SEDIMENTARY:
        return API.SEDIMENTARY_STONE;
    }
    return null;
  }

  private <T extends Entry<?>> void addIfRegistered(List<T> list, T entry) {
    if (entry.isRegistered())
      list.add(entry);
  }

  @Override
  public List<StoneEntry> allStones() {
    List<StoneEntry> stones = new ArrayList<>();
    addIfRegistered(stones, API.IGNEOUS_STONE);
    addIfRegistered(stones, API.IGNEOUS_COBBLE);
    addIfRegistered(stones, API.IGNEOUS_BRICK);
    addIfRegistered(stones, API.METAMORPHIC_STONE);
    addIfRegistered(stones, API.METAMORPHIC_COBBLE);
    addIfRegistered(stones, API.METAMORPHIC_BRICK);
    addIfRegistered(stones, API.SEDIMENTARY_STONE);
    return stones;
  }

  @Override
  public List<StoneEntry> stonesFor(UBStoneType type) {
    List<StoneEntry> stones = new ArrayList<>();
    for (UBStoneStyle style : UBStoneStyle.values())
      addIfRegistered(stones, getStoneEntry(type, style));
    return stones;
  }

  @Override
  public List<StoneEntry> stonesFor(UBStoneType type1, UBStoneType type2) {
    List<StoneEntry> stones = stonesFor(type1);
    stones.addAll(stonesFor(type2));
    return stones;
  }

  @Override
  public List<StoneEntry> stonesFor(UBStoneStyle style) {
    List<StoneEntry> stones = new ArrayList<>();
    for (UBStoneType type : UBStoneType.values())
      addIfRegistered(stones, getStoneEntry(type, style));
    return stones;
  }

  @Override
  public List<StoneEntry> stonesFor(UBStoneStyle style1, UBStoneStyle style2) {
    List<StoneEntry> stones = stonesFor(style1);
    stones.addAll(stonesFor(style2));
    return stones;
  }

  @Override
  public StoneEntry stoneFor(UBStoneType type, UBStoneStyle style) {
    return getStoneEntry(type, style);
  }

  @Override
  public List<SlabEntry> allSlabs() {
    List<SlabEntry> slabs = new ArrayList<>();
    addIfRegistered(slabs, API.IGNEOUS_STONE_SLAB);
    addIfRegistered(slabs, API.IGNEOUS_COBBLE_SLAB);
    addIfRegistered(slabs, API.IGNEOUS_BRICK_SLAB);
    addIfRegistered(slabs, API.METAMORPHIC_STONE_SLAB);
    addIfRegistered(slabs, API.METAMORPHIC_COBBLE_SLAB);
    addIfRegistered(slabs, API.METAMORPHIC_BRICK_SLAB);
    addIfRegistered(slabs, API.SEDIMENTARY_STONE_SLAB);
    return slabs;
  }

  @Override
  public List<SlabEntry> slabsFor(UBStoneType type) {
    List<SlabEntry> slabs = new ArrayList<>();
    for (UBStoneStyle style : UBStoneStyle.values())
      addIfRegistered(slabs, getStoneEntry(type, style).getSlab());
    return slabs;
  }

  @Override
  public List<SlabEntry> slabsFor(UBStoneType type1, UBStoneType type2) {
    List<SlabEntry> slabs = slabsFor(type1);
    slabs.addAll(slabsFor(type2));
    return slabs;
  }

  @Override
  public List<SlabEntry> slabsFor(UBStoneStyle style) {
    List<SlabEntry> slabs = new ArrayList<>();
    for (UBStoneType type : UBStoneType.values())
      addIfRegistered(slabs, getStoneEntry(type, style).getSlab());
    return slabs;
  }

  @Override
  public List<SlabEntry> slabsFor(UBStoneStyle style1, UBStoneStyle style2) {
    List<SlabEntry> slabs = slabsFor(style1);
    slabs.addAll(slabsFor(style2));
    return slabs;
  }

  @Override
  public SlabEntry slabFor(UBStoneType type, UBStoneStyle style) {
    return getStoneEntry(type, style).getSlab();
  }

  @Override
  public List<ButtonEntry> allButtons() {
    List<ButtonEntry> buttons = new ArrayList<>();
    addIfRegistered(buttons, API.IGNEOUS_STONE_BUTTON);
    addIfRegistered(buttons, API.IGNEOUS_COBBLE_BUTTON);
    addIfRegistered(buttons, API.IGNEOUS_BRICK_BUTTON);
    addIfRegistered(buttons, API.METAMORPHIC_STONE_BUTTON);
    addIfRegistered(buttons, API.METAMORPHIC_COBBLE_BUTTON);
    addIfRegistered(buttons, API.METAMORPHIC_BRICK_BUTTON);
    addIfRegistered(buttons, API.SEDIMENTARY_STONE_BUTTON);
    return buttons;
  }

  @Override
  public List<ButtonEntry> buttonsFor(UBStoneType type) {
    List<ButtonEntry> buttons = new ArrayList<>();
    for (UBStoneStyle style : UBStoneStyle.values())
      addIfRegistered(buttons, getStoneEntry(type, style).getButton());
    return buttons;
  }

  @Override
  public List<ButtonEntry> buttonsFor(UBStoneType type1, UBStoneType type2) {
    List<ButtonEntry> buttons = buttonsFor(type1);
    buttons.addAll(buttonsFor(type2));
    return buttons;
  }

  @Override
  public List<ButtonEntry> buttonsFor(UBStoneStyle style) {
    List<ButtonEntry> buttons = new ArrayList<>();
    for (UBStoneType type : UBStoneType.values())
      addIfRegistered(buttons, getStoneEntry(type, style).getButton());
    return buttons;
  }

  @Override
  public List<ButtonEntry> buttonsFor(UBStoneStyle style1, UBStoneStyle style2) {
    List<ButtonEntry> buttons = buttonsFor(style1);
    buttons.addAll(buttonsFor(style2));
    return buttons;
  }

  @Override
  public ButtonEntry buttonFor(UBStoneType type, UBStoneStyle style) {
    return getStoneEntry(type, style).getButton();
  }

  @Override
  public List<StairsEntry> allStairs() {
    List<StairsEntry> stairs = new ArrayList<>();
    addIfRegistered(stairs, API.IGNEOUS_STONE_STAIRS);
    addIfRegistered(stairs, API.IGNEOUS_COBBLE_STAIRS);
    addIfRegistered(stairs, API.IGNEOUS_BRICK_STAIRS);
    addIfRegistered(stairs, API.METAMORPHIC_STONE_STAIRS);
    addIfRegistered(stairs, API.METAMORPHIC_COBBLE_STAIRS);
    addIfRegistered(stairs, API.METAMORPHIC_BRICK_STAIRS);
    addIfRegistered(stairs, API.SEDIMENTARY_STONE_STAIRS);
    return stairs;
  }

  @Override
  public List<StairsEntry> stairsFor(UBStoneType type) {
    List<StairsEntry> stairs = new ArrayList<>();
    for (UBStoneStyle style : UBStoneStyle.values())
      addIfRegistered(stairs, getStoneEntry(type, style).getStairs());
    return stairs;
  }

  @Override
  public List<StairsEntry> stairsFor(UBStoneType type1, UBStoneType type2) {
    List<StairsEntry> stairs = stairsFor(type1);
    stairs.addAll(stairsFor(type2));
    return stairs;
  }

  @Override
  public List<StairsEntry> stairsFor(UBStoneStyle style) {
    List<StairsEntry> stairs = new ArrayList<>();
    for (UBStoneType type : UBStoneType.values())
      addIfRegistered(stairs, getStoneEntry(type, style).getStairs());
    return stairs;
  }

  @Override
  public List<StairsEntry> stairsFor(UBStoneStyle style1, UBStoneStyle style2) {
    List<StairsEntry> stairs = stairsFor(style1);
    stairs.addAll(stairsFor(style2));
    return stairs;
  }

  @Override
  public StairsEntry stairsFor(UBStoneType type, UBStoneStyle style) {
    return getStoneEntry(type, style).getStairs();
  }

  @Override
  public List<WallEntry> allWalls() {
    List<WallEntry> walls = new ArrayList<>();
    addIfRegistered(walls, API.IGNEOUS_STONE_WALL);
    addIfRegistered(walls, API.IGNEOUS_COBBLE_WALL);
    addIfRegistered(walls, API.IGNEOUS_BRICK_WALL);
    addIfRegistered(walls, API.METAMORPHIC_STONE_WALL);
    addIfRegistered(walls, API.METAMORPHIC_COBBLE_WALL);
    addIfRegistered(walls, API.METAMORPHIC_BRICK_WALL);
    addIfRegistered(walls, API.SEDIMENTARY_STONE_WALL);
    return walls;
  }

  @Override
  public List<WallEntry> wallsFor(UBStoneType type) {
    List<WallEntry> walls = new ArrayList<>();
    for (UBStoneStyle style : UBStoneStyle.values())
      addIfRegistered(walls, getStoneEntry(type, style).getWall());
    return walls;
  }

  @Override
  public List<WallEntry> wallsFor(UBStoneType type1, UBStoneType type2) {
    List<WallEntry> walls = wallsFor(type1);
    walls.addAll(wallsFor(type2));
    return walls;
  }

  @Override
  public List<WallEntry> wallsFor(UBStoneStyle style) {
    List<WallEntry> walls = new ArrayList<>();
    for (UBStoneType type : UBStoneType.values())
      addIfRegistered(walls, getStoneEntry(type, style).getWall());
    return walls;
  }

  @Override
  public List<WallEntry> wallsFor(UBStoneStyle style1, UBStoneStyle style2) {
    List<WallEntry> walls = wallsFor(style1);
    walls.addAll(wallsFor(style2));
    return walls;
  }

  @Override
  public WallEntry wallFor(UBStoneType type, UBStoneStyle style) {
    return getStoneEntry(type, style).getWall();
  }

}
