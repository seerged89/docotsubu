package model;

import java.util.List;

import dao.MutterDAO;

public class MutterLogic {

	public List<Mutter> loadMutter() {
		MutterDAO dao = new MutterDAO();
		List<Mutter> mutterList = dao.load();
		return mutterList;
	}

	public boolean addMutter(Mutter mutter, User user) {
		MutterDAO dao = new MutterDAO();
		boolean bo = dao.add(mutter, user);
		return bo;
	}

	public boolean likeMutter(int id, int like) {
		MutterDAO dao = new MutterDAO();
		boolean bo = dao.like(id, like);
		return bo;
	}

	public boolean dislikeMutter(int id, int dislike) {
		MutterDAO dao = new MutterDAO();
		boolean bo = dao.dislike(id, dislike);
		return bo;
	}

	public boolean deleteMutter(int id) {
		MutterDAO dao = new MutterDAO();
		boolean bo = dao.delete(id);
		return bo;
	}

	public boolean editMutter(Mutter mutter) {
		MutterDAO dao = new MutterDAO();
		boolean bo = dao.edit(mutter);
		return bo;
	}


}
