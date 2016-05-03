//
// EvhListAllChildrenOrganizationsCommand.h
// generated at 2016-04-29 18:56:00 
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

