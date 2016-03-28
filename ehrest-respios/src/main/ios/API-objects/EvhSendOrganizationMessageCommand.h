//
// EvhSendOrganizationMessageCommand.h
// generated at 2016-03-25 19:05:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendOrganizationMessageCommand
//
@interface EvhSendOrganizationMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* communityIds;

@property(nonatomic, copy) NSString* message;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

