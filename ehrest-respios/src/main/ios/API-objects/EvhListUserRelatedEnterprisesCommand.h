//
// EvhListUserRelatedEnterprisesCommand.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserRelatedEnterprisesCommand
//
@interface EvhListUserRelatedEnterprisesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

