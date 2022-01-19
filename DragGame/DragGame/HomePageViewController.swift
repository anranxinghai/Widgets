
//
//  HomePageViewController.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/5.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
import UIKit

let SCREENWIDTH = UIScreen.main.bounds.width
let SCREENHEIGHT = UIScreen.main.bounds.height
class HomePageViewController:UIViewController{
    private var uiTable:UITableView?
    private var dataSource = ["GAME","UILabel","UIButton","UIImageView","UILayer","UIParentView","GestureRecognizerViewController","UIControlViewController","UICVController"]
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        navigationItem.title = "我是首页"
        
        uiTable = UITableView.init(frame: view.bounds)
        uiTable?.dataSource = self
        uiTable?.delegate = self
        uiTable?.register(UITableViewCell.self, forCellReuseIdentifier: "UITableViewCell")
        view.addSubview(uiTable!)
        
        
    }
}

extension HomePageViewController: UITableViewDataSource{
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSource.endIndex
    }
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "UITableViewCell", for: indexPath)
        cell.accessoryType = .disclosureIndicator
        cell.textLabel?.text = dataSource[indexPath.row]
        if( indexPath.row < dataSource.endIndex){
            cell.textLabel?.text = dataSource[indexPath.row]
        }
        return cell
    }
    
}

extension HomePageViewController:UITableViewDelegate{
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
        var vc:UIViewController? = nil
        switch indexPath.row{
        case 0:
//            let storyboard = UIStoryboard.init(name: "Main", bundle: nil)
            
//            vc = storyboard.instantiateViewController(withIdentifier: "ViewController")
            vc = ViewController.init()
        case 1:
            vc = UILabelViewController.init()
        case 2:
            vc = UIButtonViewController.init()
        case 3:
            vc = UIImageViewController.init()
        case 4:
            vc = UILayerViewController.init()
        case 5:
            vc = UIParentViewController.init()
        case 6:
            vc = GestureRecognizerViewController.init()
        case 7:
            vc = UIControlViewController.init()
        case 8:
            vc = UICVController.init()
        default:
            break
        }
        
        self.navigationController?.pushViewController(vc!, animated: true)
        
    }
}

