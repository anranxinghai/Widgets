//
//  UIImageViewController.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/10.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
class UIImageViewController:UIViewController{
    private var uiImage:UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        uiImage = UIImageView.init()
        view.addSubview(uiImage)
        uiImage.translatesAutoresizingMaskIntoConstraints = false
        uiImage.topAnchor.constraint(equalTo: view.topAnchor,constant: 100).isActive = true
        uiImage.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        uiImage.widthAnchor.constraint(equalToConstant: 100).isActive = true
        uiImage.heightAnchor.constraint(equalToConstant: 100).isActive = true
        let image = UIImage.init(imageLiteralResourceName: "anranxinghai.png")
        uiImage.image = image
        
        
    }
}
