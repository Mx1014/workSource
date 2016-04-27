//
// EvhListAllChildrenOrganizationsCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAllChildrenOrganizationsCommand
//
@interface EvhListAllChildrenOrganizationsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* groupTypes;

@property(nonatomic, copy) NSNumber* naviFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

