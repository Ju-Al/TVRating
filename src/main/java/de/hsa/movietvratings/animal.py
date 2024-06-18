class Animal:
    def __init__(self, name: str):
            self.name = name
    
    def get_name(self):
        return self.name
    
    def make_sound(self):
        pass

    def animal_leg_count(animals: list):
        for animal in animals:
            if isinstance(animal, Lion):
                print(lion_leg_count(animal))
            elif isinstance(animal, Mouse):
                print(mouse_leg_count(animal))
            elif isinstance(animal, Pigeon):
                print(pigeon_leg_count(animal))


class Pigeon(Animal):
    def make_sound(self):
        return 'coo'


class Lion(Animal):
    def make_sound(self):
        return 'roar'


class Mouse(Animal):
    def make_sound(self):
        return 'squeak'


class Snake(Animal):
    def make_sound(self):
        return 'hiss'


def animal_sound(animals: list):
    for animal in animals:
        print(animal.make_sound())

