/*******************************************************************************
 * Copyright (c) 2014 Agnie Technologies.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Agnie Technologies - initial API and implementation
 ******************************************************************************/
package com.agnie.common.gwt.serverclient.client.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.agnie.common.gwt.serverclient.client.helper.InvalidPermission;

/**
 * To hold permission information in tree structure with assumption that every permission string starts with "perm"
 * string.
 * 
 */
public class AccessControlList implements Serializable {

	private static final String	ROOT_NODE			= "perm";
	private static final String	SEPERATOR			= "_";

	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;

	/*
	 * root "perm" node
	 */
	private Node				root;

	private boolean				owner				= false;

	/*
	 * Inner node class to hold individual node information separated by SEPERATOR
	 */
	public static class Node implements Serializable {
		/**
         * 
         */
		private static final long	serialVersionUID	= 1L;
		private String				code;
		@SuppressWarnings("unused")
		private Node				parent;
		private List<Node>			childs				= new ArrayList<Node>();

		@Override
		public int hashCode() {
			if (code != null)
				return code.hashCode();
			else
				return 0;
		}

		@Override
		public boolean equals(Object obj) {

			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (code == null) {
				if (other.code != null)
					return false;
			} else {
				if (hashCode() != other.hashCode())
					return false;
				else if (!code.equals(other.code))
					return false;
			}
			return true;
		}

		public Node() {
		}

		public Node(String code, Node parent) {
			this.code = code;
			this.parent = parent;
		}

		/**
		 * To generate permission tree structure.
		 * 
		 * @param pm
		 * @throws InvalidPermission
		 */
		void add(String pm) throws InvalidPermission {
			if (pm != null && !("".equals(pm.trim()))) {
				String chcode = null;
				String subpm = null;
				if (pm.contains(SEPERATOR)) {
					chcode = pm.substring(0, pm.indexOf(SEPERATOR));
					subpm = pm.substring(pm.indexOf(SEPERATOR) + 1);
				} else {
					chcode = pm;
				}
				Node child = null;
				for (Node node : childs) {
					if (node.code.equals(chcode)) {
						child = node;
						break;
					}
				}
				if (child == null) {
					child = new Node(chcode, this);
					childs.add(child);
				}
				if (subpm != null && !("".equals(subpm.trim()))) {
					child.add(subpm);
				}
			}
		}

		/**
		 * Check for given permission by iterating it through tree structure
		 * 
		 * @param pm
		 * @return
		 */
		boolean check(String pm) {
			boolean resp = false;
			if (pm.startsWith(this.code)) {
				if (pm.contains(SEPERATOR)) {
					if (childs.size() > 0 && this.code.equals(pm.substring(0, pm.indexOf(SEPERATOR)))) {
						String subpm = pm.substring(pm.indexOf(SEPERATOR) + 1);
						for (Node child : childs) {
							if (child.check(subpm)) {
								resp = true;
								break;
							}
						}
					}
				} else if (pm.equals(code)) {
					resp = true;
				}
			}
			return resp;
		}

		private StringBuffer toString(StringBuffer str, String prntstr) {
			String newStr = null;
			if (prntstr != null && !("".equals(prntstr.trim()))) {
				newStr = prntstr + SEPERATOR + code;
			} else {
				newStr = code;
			}

			if (childs.size() > 0) {
				for (Node node : childs) {
					node.toString(str, newStr);
				}
			} else {
				str.append(newStr + "\n");
			}
			return str;
		}

		@Override
		public String toString() {
			return toString(new StringBuffer(), "").toString();
		}
	}

	public AccessControlList() {
	}

	public AccessControlList(boolean owner) {
		this.owner = owner;
	}

	/**
	 * @return the owner
	 */
	boolean isOwner() {
		return owner;
	}

	/**
	 * Add new permission into tree structure
	 * 
	 * @param pm
	 * @throws InvalidPermission
	 */
	public void addPermission(String pm) throws InvalidPermission {
		if (pm != null && !("".equals(pm.trim())) && pm.startsWith(ROOT_NODE + SEPERATOR)) {
			String subpm = pm.substring(pm.indexOf(SEPERATOR) + 1);
			if (subpm != null && !("".equals(subpm.trim()))) {
				if (root == null) {
					root = new Node(ROOT_NODE, null);
				}
				root.add(subpm);
			}
		} else {
			throw new InvalidPermission();
		}

	}

	/**
	 * Check if user has given permission
	 * 
	 * @param perm
	 * @return
	 */
	public boolean check(String perm) {
		return (owner ? true : (root != null ? root.check(perm) : false));
	}

	@Override
	public String toString() {
		return root.toString();
	}
}
