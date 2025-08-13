# VoidOpal

A **utility library for Spigot plugin development** that cuts down boilerplate and gives you powerful, opinionated tools for building Minecraft plugins faster.

Why this one and not another?
Because VoidOpal is optimised for faster code development, as opposed to a fully unbreakable and overly complicated system of builder. Once you learn the ropes, you'll be spending a lot less time typing

*"All I want is a /myplugin give command!"*

Successor of [ObsidianMC](https://github.com/stormer3428/ObsidianMC)

---

## Features

### **Custom Command System**  
Define commands using from a simple string:
  - Automatic permission generation
  - Built-in autocompletion
  - Parsing


<details>
  <summary>Code example</summary>
  
```java
registerCommand(new OMCCommand("myplugin item give %ITEM%", true) {
  @Override public boolean execute(CommandSender sender, ArrayList<String> args) {
    /* code here */
  }
});
```
That's it, this little snippet handles all permissions, autocompletion, and gathers all arguments neatly into a list for you. Nothing more, nothing less

Want aliases? sure! just separate your argument with `%%%` and the parser does the rest, for example `myplugin%%%mp item%%%i give%%%g %ITEM%` would accept :
  - `myplugin item give supersword`
  - `mp item give supersword`
  - `mp i g supersword`

what's the permission string? simple, take all the argument names (remove all the variables) and merge them with dots:
  - `myplugin%%%mp item%%%i give%%%g %ITEM%` needs the permission `myplugin.item.give`

Don't want the command to need permissions? just change the boolean to false! (or omit the parameter entirely)
</details>

### **Custom Item Support**
  - Automatic per-item configuration files
  - Simplifies creation and management of custom items

<details>
  <summary>Code example</summary>
The system is intended to be used in an enum like the following : 

```java
public enum ItemType {
  CHAIN_CRYSTAL(new SMPItem("chainCrystal")
          .setMaterial(Material.NETHER_STAR)
          .setDisplayname("Chain Crystal")
          .addPower(PowerType.CHAIN_CRYSTAL.omcPower)
          ),

  CHAIN_SCEPTER(new SMPItem("chainScepter")
          .setMaterial(Material.CARROT_ON_A_STICK)
          .setDisplayname("#cc99ff&o&lChain Scepter")
          .setLore(Arrays.asList(
                "",
                OMCUtil.translateChatColor("#cc99ff&o&lSPELLS"),
                "",
                OMCUtil.translateChatColor("#cc99ff&oChain Hook"),
                OMCUtil.translateChatColor("#cc99ff&oChain Rift"),
                OMCUtil.translateChatColor("#cc99ff&o$lCrucifix Chain"),
                ""
                  ))
          .unbreakable()
          .addPower(PowerType.CHAIN_SCEPTER.omcPower)
          .addTickeable((OMCTickable) PowerType.CHAIN_SCEPTER.omcPower)
          );

  public final OMCItem omcItem;

  private ItemType(OMCItem item) {
                  this.omcItem = item;
  }
}
```
</details>

### **In-code Annotation-Based Configuration System**
- Populate `public static` fields directly from config files using Java annotations
  - No repetitive manual parsing
  - Automatic configuration file repair upon encountering missing values
  - In code configurations (no stray config files to update all the time!)

```java
@AutoConfig(config = "config/powers/myClass.yml")
public MyClass {

  @IntConfigValue(defaultValue = 20 * 60 * 10, path = "cooldown")
  public static int COOLDOWN;

  @DoubleConfigValue(defaultValue = 2.0, path = "force")
  public static double FORCE = 0;
}
```

### **JavaPlugin Wrapper**  
  A convenience wrapper for `JavaPlugin` with integrated helpers.

### **Logging**  
  An internal logger with fancy colors! (exciting I know)

### **Advanced Particle Library**
  - Create complex particle effects using vector math
  - Supports 3D model-like structures

---

## Installation

### Build & Install Locally
```bash
git clone https://github.com/stormer3428/VoidOpal.git
cd VoidOpal
mvn clean install
```
Use in Your Plugin
Just add it as a dependency in your pom.xml:

```xml
<dependency>
  <groupId>fr.stormer3428</groupId>
  <artifactId>VoidOpal</artifactId>
  <version>0.3.0</version> <!-- Replace with actual version -->
</dependency>
```
