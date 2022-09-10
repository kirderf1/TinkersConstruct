package slimeknights.tconstruct.tables.client.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import slimeknights.mantle.client.screen.ElementScreen;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.client.Icons;
import slimeknights.tconstruct.tables.client.inventory.module.SideInventoryScreen;
import slimeknights.tconstruct.tables.client.inventory.widget.TinkerTabsWidget;
import slimeknights.tconstruct.tables.menu.TabbedContainerMenu;
import slimeknights.tconstruct.tables.menu.module.SideInventoryContainer;

import javax.annotation.Nullable;

public class BaseTabbedScreen<TILE extends BlockEntity, CONTAINER extends TabbedContainerMenu<TILE>> extends ExtendedContainerScreen<CONTAINER> {
  protected static final Component COMPONENT_WARNING = TConstruct.makeTranslation("gui", "warning");
  protected static final Component COMPONENT_ERROR = TConstruct.makeTranslation("gui", "error");

  public static final ResourceLocation BLANK_BACK = TConstruct.getResource("textures/gui/blank.png");

  @Nullable
  protected final TILE tile;
  protected TinkerTabsWidget tabsScreen;

  public BaseTabbedScreen(CONTAINER container, Inventory playerInventory, Component title) {
    super(container, playerInventory, title);
    this.tile = container.getTile();
  }

  @Override
  protected void init() {
    super.init();

    this.tabsScreen = addExtraArea(addRenderableWidget(new TinkerTabsWidget(this)));
  }

  @Nullable
  public TILE getTileEntity() {
    return this.tile;
  }

  protected void drawIcon(PoseStack matrices, Slot slot, ElementScreen element) {
    RenderSystem.setShaderTexture(0, Icons.ICONS);
    element.draw(matrices, slot.x + this.cornerX - 1, slot.y + this.cornerY - 1);
  }

  protected void drawIconEmpty(PoseStack matrices, Slot slot, ElementScreen element) {
    if (slot.hasItem()) {
      return;
    }

    this.drawIcon(matrices, slot, element);
  }

  public void error(Component message) {
  }

  public void warning(Component message) {
  }

  public void updateDisplay() {
  }

  protected void addChestSideInventory(Inventory inventory) {
    SideInventoryContainer<?> sideInventoryContainer = getMenu().getSubContainer(SideInventoryContainer.class);
    if (sideInventoryContainer != null) {
      // no title if missing one
      Component sideInventoryName = TextComponent.EMPTY;
      BlockEntity te = sideInventoryContainer.getTile();
      if (te instanceof MenuProvider) {
        sideInventoryName = ((MenuProvider) te).getDisplayName();
      }

      this.addModule(new SideInventoryScreen<>(this, sideInventoryContainer, inventory, sideInventoryName, sideInventoryContainer.getSlotCount(), sideInventoryContainer.getColumns()));
    }
  }
}
