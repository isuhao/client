package bspkrs.util.config.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** @deprecated */
@Deprecated
public class GuiSelectString extends GuiScreen {
   protected GuiScreen parentScreen;
   protected IConfigProperty prop;
   private GuiSelectStringEntries guiScrollList;
   private GuiButtonExt btnUndoChanges;
   private GuiButtonExt btnDefault;
   private GuiButtonExt btnDone;
   private String title;
   protected String titleLine2;
   protected String titleLine3;
   protected int slotIndex;
   private final Map selectableValues;
   private final String beforeValue;
   private String currentValue;
   private HoverChecker tooltipHoverChecker;
   private List toolTip;
   protected boolean enabled;

   public GuiSelectString(GuiScreen parentScreen, IConfigProperty prop, int slotIndex, Map selectableValues, String currentValues, boolean enabled) {
      this.mc = Minecraft.getMinecraft();
      this.parentScreen = parentScreen;
      this.prop = prop;
      this.slotIndex = slotIndex;
      this.selectableValues = selectableValues;
      this.beforeValue = currentValues;
      this.currentValue = currentValues;
      this.toolTip = new ArrayList();
      this.enabled = enabled;
      String propName = I18n.format(prop.getLanguageKey(), new Object[0]);
      String comment;
      if(prop.getType().equals(ConfigGuiType.INTEGER)) {
         comment = I18n.format(prop.getLanguageKey() + ".tooltip", new Object[]{"\n" + EnumChatFormatting.AQUA, prop.getDefault(), Integer.valueOf(prop.getMinIntValue()), Integer.valueOf(prop.getMaxIntValue())});
      } else {
         comment = I18n.format(prop.getLanguageKey() + ".tooltip", new Object[]{"\n" + EnumChatFormatting.AQUA, prop.getDefault(), Double.valueOf(prop.getMinDoubleValue()), Double.valueOf(prop.getMaxDoubleValue())});
      }

      if(!comment.equals(prop.getLanguageKey() + ".tooltip")) {
         this.toolTip = this.mc.fontRenderer.listFormattedStringToWidth(EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + comment, 300);
      } else if(prop.getComment() != null && !prop.getComment().trim().isEmpty()) {
         this.toolTip = this.mc.fontRenderer.listFormattedStringToWidth(EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + prop.getComment(), 300);
      } else {
         this.toolTip = this.mc.fontRenderer.listFormattedStringToWidth(EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.RED + "No tooltip defined.", 300);
      }

      if(parentScreen instanceof GuiConfig) {
         this.title = ((GuiConfig)parentScreen).title;
         this.titleLine2 = ((GuiConfig)parentScreen).titleLine2;
         this.titleLine3 = I18n.format(prop.getLanguageKey(), new Object[0]);
         this.tooltipHoverChecker = new HoverChecker(28, 37, 0, parentScreen.width, 800);
      } else {
         this.title = I18n.format(prop.getLanguageKey(), new Object[0]);
         this.tooltipHoverChecker = new HoverChecker(8, 17, 0, parentScreen.width, 800);
      }

   }

   public void initGui() {
      this.guiScrollList = new GuiSelectStringEntries(this, this.mc, this.prop, this.selectableValues, this.beforeValue, this.currentValue);
      int doneWidth = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("gui.done", new Object[0])) + 20, 100);
      int undoWidth = this.mc.fontRenderer.getStringWidth("↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges", new Object[0])) + 20;
      int resetWidth = this.mc.fontRenderer.getStringWidth("☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault", new Object[0])) + 20;
      int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
      this.buttonList.add(this.btnDone = new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done", new Object[0])));
      this.buttonList.add(this.btnDefault = new GuiButtonExt(2001, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, "☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault", new Object[0])));
      this.buttonList.add(this.btnUndoChanges = new GuiButtonExt(2002, this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, "↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges", new Object[0])));
   }

   protected void actionPerformed(GuiButton button) {
      if(button.id == 2000) {
         try {
            this.guiScrollList.saveChanges();
         } catch (Throwable var3) {
            var3.printStackTrace();
         }

         this.mc.displayGuiScreen(this.parentScreen);
      } else if(button.id == 2001) {
         this.currentValue = this.prop.getDefault();
         this.guiScrollList = new GuiSelectStringEntries(this, this.mc, this.prop, this.selectableValues, this.beforeValue, this.currentValue);
      } else if(button.id == 2002) {
         this.currentValue = this.beforeValue;
         this.guiScrollList = new GuiSelectStringEntries(this, this.mc, this.prop, this.selectableValues, this.beforeValue, this.currentValue);
      }

   }

   protected void mouseMovedOrUp(int x, int y, int mouseEvent) {
      if(mouseEvent != 0 || !this.guiScrollList.func_148181_b(x, y, mouseEvent)) {
         super.mouseMovedOrUp(x, y, mouseEvent);
      }
   }

   public void drawScreen(int par1, int par2, float par3) {
      this.drawDefaultBackground();
      this.guiScrollList.func_148128_a(par1, par2, par3);
      this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);
      if(this.titleLine2 != null) {
         this.drawCenteredString(this.fontRendererObj, this.titleLine2, this.width / 2, 18, 16777215);
      }

      if(this.titleLine3 != null) {
         this.drawCenteredString(this.fontRendererObj, this.titleLine3, this.width / 2, 28, 16777215);
      }

      this.btnDefault.enabled = this.enabled && !this.guiScrollList.isDefault();
      this.btnUndoChanges.enabled = this.enabled && this.guiScrollList.isChanged();
      super.drawScreen(par1, par2, par3);
      if(this.tooltipHoverChecker != null && this.tooltipHoverChecker.checkHover(par1, par2)) {
         this.drawToolTip(this.toolTip, par1, par2);
      }

   }

   public void drawToolTip(List stringList, int x, int y) {
      //this.drawHoveringText(stringList, x, y, this.fontRendererObj);
      this.func_146283_a(stringList, x, y);
   }
}
