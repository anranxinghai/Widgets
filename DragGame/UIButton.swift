//
//  UIButton.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/9.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
class UIButtonViewController:UIViewController{
    private var uiButton:UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        navigationItem.title = "UIButton"
        uiButton = UIButton.init()
        uiButton.setTitle("我是UIButton", for: .normal)
        uiButton.addTarget(self, action: #selector(buttonClick), for: .touchUpInside)
        
        view.addSubview(uiButton)
        uiButton.translatesAutoresizingMaskIntoConstraints = false
        uiButton.centerXAnchor.constraint(equalTo: view.centerXAnchor,constant: 0).isActive = true
        uiButton.setTitleColor(.red, for: .normal)
        uiButton.topAnchor.constraint(equalTo: view.topAnchor,constant:  100).isActive = true
        uiButton.widthAnchor.constraint(equalToConstant: 100).isActive = true
        uiButton.heightAnchor.constraint(equalToConstant: 50).isActive = true
        
    }
    
    @objc func buttonClick(sender:UIButton){
        sender.isSelected.toggle()
//        sender.isSelected  = !sender.isSelected
        if(sender.isSelected) {
            uiButton.setTitle("我被选中了", for: .normal)
        }
        else {
            uiButton.setTitle("没有选中我", for: .normal)
        }
            
    }
}
