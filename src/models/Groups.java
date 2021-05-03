package models;

public class Groups extends SNS {
	private
		int groupId;
		String groupName;


		public int getGroupId() {
			return groupId;
		}
		public void setGroupId(int groupId) {
			this.groupId = groupId;
		}
		public String getGroupName() {
			return groupName;
		}
		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

	@Override
	public String toString() {
		return 	"groupId=" + groupId +
				", groupName='" + groupName + '\'' +
				", creationDate=" + creationDate +
				", creator=" + creator.getMember_id() +
				", genre='" + genre + '\'';
	}
}
