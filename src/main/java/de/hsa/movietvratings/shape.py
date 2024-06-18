class IShape:
    def draw_square(self):
        raise NotImplementedError
    
    def draw_rectangle(self):
        raise NotImplementedError
    
    def draw_circle(self):
        raise NotImplementedError

class Circle(IShape):
    def draw_square(self):
        print("Cannot draw square: Circle does not have straight sides")

    def draw_rectangle(self):
        print("Cannot draw rectangle: Circle does not have straight sides")
    
    def draw_circle(self):
        print("Circle drawn")

class Square(IShape):
    def draw_square(self):
        print("Square drawn")

    def draw_rectangle(self):
        print("Rectangle drawn for Square (assuming all sides are equal)")
    
    def draw_circle(self):
        print("Cannot draw circle: Square does not have a curved boundary")

class Rectangle(IShape):
    def draw_square(self):
        print("Square drawn for Rectangle (assuming opposite sides are equal)")

    def draw_rectangle(self):
        print("Rectangle drawn")
    
    def draw_circle(self):
        print("Cannot draw circle: Rectangle does not have a curved boundary")
