class Animal:
    def __init__(self, name: str):
            self.name = name
    
    def get_name(self):
        pass
   
animals = [
    Animal('lion'),
    Animal('mouse')
]

def animal_sound(animals: list):
    for animal in animals:
        if animal.name == 'lion':
            print('roar')

        elif animal.name == 'mouse':
            print('squeak')

animal_sound(animals)