//
// EvhSendMessageAdminCommand.h
// generated at 2016-04-22 13:56:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendMessageAdminCommand
//
@interface EvhSendMessageAdminCommand
    : NSObject<EvhJsonSerializable>


// item type NSString*
@property(nonatomic, strong) NSMutableDictionary* meta;

@property(nonatomic, copy) NSString* bodyType;

@property(nonatomic, copy) NSString* body;

@property(nonatomic, copy) NSNumber* deliveryOption;

@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSNumber* targetToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

