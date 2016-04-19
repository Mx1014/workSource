//
// EvhApproveCommunityAdminCommand.h
// generated at 2016-04-19 12:41:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApproveCommunityAdminCommand
//
@interface EvhApproveCommunityAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* operatorRole;

@property(nonatomic, copy) NSString* reason;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

