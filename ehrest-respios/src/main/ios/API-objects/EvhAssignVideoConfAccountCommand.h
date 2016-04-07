//
// EvhAssignVideoConfAccountCommand.h
// generated at 2016-04-07 17:03:16 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAssignVideoConfAccountCommand
//
@interface EvhAssignVideoConfAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSNumber* accountId;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* contactId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

