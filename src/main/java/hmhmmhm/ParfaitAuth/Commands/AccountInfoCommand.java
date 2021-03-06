package hmhmmhm.ParfaitAuth.Commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.TaskHandler;
import hmhmmhm.ParfaitAuth.ParfaitAuthPlugin;
import hmhmmhm.ParfaitAuth.PlayerIdentifier;
import hmhmmhm.ParfaitAuth.Tasks.SendAccountInfoTask;
import hmhmmhm.ParfaitAuth.Tasks.SendMessageTask;

public class AccountInfoCommand extends ParfaitAuthCommand {
	public AccountInfoCommand(ParfaitAuthPlugin plugin) {
		super(plugin);
		this.load("account-info", true);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// /a <필터> <검색어>
		// 밴처리를 위해 해당 유저의 계정정보를 찾습니다.
		// (사용가능필터, 아이디, 닉네임, 식별번호)
		// (아이디 혹은 닉네임을 정확하게 입력해야합니다.)

		if (command.getName().toLowerCase() == this.commandName) {
			if (args.length < 2) {
				SendMessageTask task = new SendMessageTask(sender, this.commandKey + "-help-");
				TaskHandler handler = this.getServer().getScheduler().scheduleRepeatingTask(task, 10);
				task.setHandler(handler);
				return true;
			}

			// /a 아이디 <검색어>
			if (args[0].equals(this.getMessage(this.commandKey + "-sub-id"))) {
				sender.sendMessage(this.getPlugin().getMessage("status-start-the-async-db-request"));
				SendAccountInfoTask task = new SendAccountInfoTask();

				task.id = args[1];
				task.sender = sender.getName();

				this.getServer().getScheduler().scheduleAsyncTask(task);
				return true;
			}

			// /a 닉네임 <검색어>
			if (args[0].equals(this.getMessage(this.commandKey + "-sub-nick"))) {
				sender.sendMessage(this.getPlugin().getMessage("status-start-the-async-db-request"));
				SendAccountInfoTask task = new SendAccountInfoTask();

				task.nick = args[1];
				task.sender = sender.getName();

				this.getServer().getScheduler().scheduleAsyncTask(task);
				return true;
			}

			// /a 식별번호 <검색어>
			if (args[0].equals(this.getMessage(this.commandKey + "-sub-identy"))) {
				sender.sendMessage(this.getPlugin().getMessage("status-start-the-async-db-request"));
				SendAccountInfoTask task = new SendAccountInfoTask();

				// 식별번호 확인
				String identifierString = null;
				int identifierInt;

				// [1]과 같은 형태로 입력되면 숫자만 분리
				if (args[1].split("\\[").length == 2 && args[1].split("\\[")[1].split("\\]").length == 1)
					identifierString = args[1].split("\\[")[1].split("\\]")[0];

				if (identifierString == null)
					identifierString = args[1];

				// 정수형으로 변환
				try {
					identifierInt = Integer.valueOf(identifierString);
				} catch (NumberFormatException e) {
					sender.sendMessage(plugin.getMessage("error-cant-find-player-identifier"));
					return true;
				}

				task.identy = PlayerIdentifier.get(identifierInt).toString();
				task.sender = sender.getName();

				this.getServer().getScheduler().scheduleAsyncTask(task);
				return true;
			}
		}
		return false;
	}
}
