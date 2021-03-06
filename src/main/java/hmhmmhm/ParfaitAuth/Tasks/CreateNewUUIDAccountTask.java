package hmhmmhm.ParfaitAuth.Tasks;

import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import hmhmmhm.ParfaitAuth.Account;
import hmhmmhm.ParfaitAuth.ParfaitAuth;
import hmhmmhm.ParfaitAuth.ParfaitAuthPlugin;

public class CreateNewUUIDAccountTask extends AsyncTask {
	private UUID uuid = null;
	private String username = null;
	private Account account = null;
	private Account oldAccountData = null;
	private int result;

	public CreateNewUUIDAccountTask(UUID uuid, String username) {
		this.uuid = uuid;
		this.username = username;
	}

	public CreateNewUUIDAccountTask(Account oldAccountData, UUID uuid, String username) {
		this.oldAccountData = oldAccountData;
		this.uuid = uuid;
		this.username = username;
	}

	@Override
	public void onRun() {
		if (this.oldAccountData != null)
			ParfaitAuth.updateAccount_id(oldAccountData._id, oldAccountData.convertToDocument());

		this.result = ParfaitAuth.addAccount(this.uuid, ParfaitAuth.getRandomName());
		this.account = ParfaitAuth.getAccount(this.uuid);
	}

	@Override
	public void onCompletion(Server server) {
		Player player = server.getPlayer(this.username);

		// 플레이어가 접속 중이 아니면 반환
		if (player == null || !player.isConnected())
			return;

		switch (result) {
		case ParfaitAuth.CLIENT_IS_DEAD:
			player.sendMessage(ParfaitAuthPlugin.getPlugin().getMessage("error-db-connect-unstable"));
			break;
		case ParfaitAuth.ALREADY_EXIST_ACCOUNT:
			ParfaitAuth.authorization(player, this.account);
			break;
		case ParfaitAuth.SUCCESS:
			player.sendMessage(ParfaitAuthPlugin.getPlugin().getMessage("success-uuid-account-was-created"));
			ParfaitAuth.authorizationUUID(player, this.account);
			break;
		}
	}

}
