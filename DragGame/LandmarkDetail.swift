//
//  Landmarks.swift
//  DragGame
//
//  Created by anranxinghai on 2022/2/8.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import SwiftUI

struct Landmarks: View {
    var body: some View {
        VStack {
            MapView().frame(width: 300).edgesIgnoringSafeArea(.top)
            CircleImage().offset(y:-130)
                .padding(.bottom,-130)
            LandmarkViewController()
            VStack (alignment: .leading){
                Text("Turtle Rock").font(.largeTitle)
                
                HStack {
                    
                    Text("Josha Three Nation Park").font(.subheadline)
                    Spacer()
                    Text("California")
                }
            }.padding()
            Spacer()
        }
    }
}

struct Landmarks_Previews: PreviewProvider {
    static var previews: some View {
        Landmarks()
    }
}
