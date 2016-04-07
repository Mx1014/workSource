//
// EvhListAllChildrenOrganizationsCommand.h
// generated at 2016-04-07 14:16:29 
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

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

