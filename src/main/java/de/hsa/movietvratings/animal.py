class Animal:
    def __init__(self, name: str):
            self.name = name
    
    def get_name(self):
        return self.name

    def save(self):
        # Speichert das Tier in einer Datenbank
        print(f"Saving {self.name} to the database")

    def load(self, name: str):
        # Lädt das Tier aus der Datenbank
        print(f"Loading {name} from the database")