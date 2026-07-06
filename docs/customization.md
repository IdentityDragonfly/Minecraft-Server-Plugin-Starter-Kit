# Customization

## Rename the plugin

Edit `src/main/resources/plugin.yml`:

```yaml
name: YourPluginName
main: io.github.yourname.yourplugin.YourPluginName
```

Then rename the Java package and main class.

## Add a new command

1. Create a class in `commands/`.
2. Register the command in `plugin.yml`.
3. Register the executor in `StarterPlugin.java`.

## Add a new listener

1. Create a listener class in `listeners/`.
2. Register it with `getServer().getPluginManager().registerEvents(...)`.

## Add more kit items

Edit `src/main/resources/config.yml`:

```yaml
starter-kit:
  items:
    - material: IRON_PICKAXE
      amount: 1
      name: "&bStarter Pickaxe"
```
