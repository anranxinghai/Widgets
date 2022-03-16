//
//  LandmarkViewController.swift
//  DragGame
//
//  Created by anranxinghai on 2022/3/13.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
import SwiftUI
struct LandmarkViewController
:UIViewControllerRepresentable{
    func makeUIViewController(context: Context) -> UIButtonViewController {
        let uiButtonViewController = UIButtonViewController()
        uiButtonViewController.changeName = context.coordinator
        return uiButtonViewController
    }
    
    func updateUIViewController(_ uiViewController: UIButtonViewController, context: Context) {
        
    }
    func makeCoordinator()->Coordinator{
        return Coordinator(self)
    }
    class Coordinator:NSObject,ChangeLocationNameDelegate{
        var parent:LandmarkViewController
        init(_ landMarkViewController:LandmarkViewController) {
            parent = landMarkViewController
        }
        func changeName() {
            print("Change Location")
        }
    }
    
}
