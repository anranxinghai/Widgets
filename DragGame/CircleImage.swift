//
//  CircleImage.swift
//  DragGame
//
//  Created by anranxinghai on 2022/2/9.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import SwiftUI

struct CircleImage: View {
    var body: some View {
        Image("turtlerock").clipShape(Circle())
            .overlay(Circle().stroke(.gray,lineWidth: 4).shadow(color: .green, radius: 10, x: 10, y: 20))
            
    }
}

struct CircleImage_Previews: PreviewProvider {
    static var previews: some View {
        CircleImage()
    }
}
