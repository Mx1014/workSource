//
// EvhListEnterprisesCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBoolean.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterprisesCommand
//
@interface EvhListEnterprisesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* buildingId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, strong) EvhBoolean* qryAdminRoleFlag;

@property(nonatomic, copy) NSString* keywords;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

