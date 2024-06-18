class IShape:
    def draw_square(self):
        raise NotImplementedError
    
    def draw_rectangle(self):
        raise NotImplementedError
    
    def draw_circle(self):
        raise NotImplementedError

class Circle(IShape):
    def draw_square(self):
        pass

    def draw_rectangle(self):
        pass
    
    def draw_circle(self):
        print("Circle drawn")