//
// EvhSendMessageAdminCommand.h
// generated at 2016-03-31 20:15:32 
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

