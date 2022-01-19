//
//  GestureRecognizerViewController.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/15.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
import UIKit
class GestureRecognizerViewController:UIViewController{
    private var gestureRecognizerView:DragGestureView!
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        gestureRecognizerView = DragGestureView.init(frame: CGRect.init(x: 100, y: 100, width: 100, height: 200))
        gestureRecognizerView.backgroundColor = .green
        let dragGesture = UIPanGestureRecognizer.init(target: self, action: #selector(panGesture(gesture:)))
        gestureRecognizerView.addGestureRecognizer(dragGesture)
        dragGesture.cancelsTouchesInView = true
        view.addSubview(gestureRecognizerView)
    }
    
    
    
}

class DragGestureView:UIView{
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        print("DragGesture view touchesBegan")
        
        let position = self.layer.position
        let frame = self.layer.frame
        let bounds = self.layer.bounds
        
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesMoved(touches, with: event)
        print("DragGesture view touchesMove")
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesEnded(touches, with: event)
        print("DragGesture view touchesEnded")
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesCancelled(touches, with: event)
        print("DragGesture view touchesBegan")
    }
    
    override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
        var view = super.hitTest(point, with: event)
        
        return view
    }
    
    override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {
    
        return super.point(inside: point, with: event)
    }
}

extension GestureRecognizerViewController{
    @objc private func panGesture(gesture:UIPanGestureRecognizer){
        print("正在拖动 \(gesture.state)")
    }
}
