package slimeknights.tconstruct.tables.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.tables.client.inventory.library.slots.SlotInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class SlotInformationLoader extends JsonReloadListener {

  /** GSON instance for this */
  private static final Gson GSON = new GsonBuilder()
    .setPrettyPrinting()
    .disableHtmlEscaping()
    .create();

  /** Singleton instance */
  public static final SlotInformationLoader INSTANCE = new SlotInformationLoader();

  public static final ResourceLocation REPAIR_NAME = Util.getResource("repair");

  /** Map of SlotInformation */
  private final Map<ResourceLocation, SlotInformation> slotInformationMap = new HashMap<>();

  /** Sorted List of SlotInformations */
  private final List<SlotInformation> slotInformationList = new ArrayList<>();

  private SlotInformationLoader() {
    super(GSON, "tinker_station");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> map, IResourceManager resourceManager, IProfiler profiler) {
    this.slotInformationMap.clear();
    this.slotInformationList.clear();

    for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
      ResourceLocation location = entry.getKey();
      try {
        JsonObject json = entry.getValue().getAsJsonObject();

        this.slotInformationMap.put(location, SlotInformation.fromJson(json));
      }
      catch (Exception e) {
        log.warn("Exception loading slot information '{}': {}", location, e.getMessage());
      }
    }

    this.slotInformationList.addAll(this.slotInformationMap.values());

    this.slotInformationList.sort(Comparator.comparing(SlotInformation::getSortIndex));
  }

  public static SlotInformation get(ResourceLocation registryKey) {
    return INSTANCE.slotInformationMap.get(registryKey);
  }

  public static Collection<SlotInformation> getSlotInformationList() {
    return INSTANCE.slotInformationList;
  }
}