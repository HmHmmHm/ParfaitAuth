package hmhmmhm.ParfaitAuth.Commands;

import cn.nukkit.command.CommandSender;
import hmhmmhm.ParfaitAuth.ParfaitAuthPlugin;

public class UnregisterCommand extends ParfaitAuthCommand {
	public UnregisterCommand(ParfaitAuthPlugin plugin) {
		super(plugin);
		this.load("unregister", false);
	}

	@Override
	public boolean onCommand(CommandSender sender, cn.nukkit.command.Command command, String label, String[] args) {
		if (command.getName().toLowerCase() == this.commandName) {
			// TODO
			return true;
		}
		return false;
	}
}
