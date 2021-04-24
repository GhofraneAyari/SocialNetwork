package models;

import java.time.LocalDate;

public class SNS {
	protected 
		LocalDate creationDate;
		Member creator;
		String genre;
		
		public LocalDate getCreationDate() {
			return creationDate;
		}
		public void setCreationDate(LocalDate creationDate) {
			this.creationDate = creationDate;
		}
		public Member getCreator() {
			return creator;
		}
		public void setCreator(Member creator) {
			this.creator = creator;
		}
		public String getGenre() {
			return genre;
		}
		public void setGenre(String genre) {
			this.genre = genre;
		}
	
		
}
