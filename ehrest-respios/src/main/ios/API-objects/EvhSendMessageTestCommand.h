//
// EvhSendMessageTestCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendMessageTestCommand
//
@interface EvhSendMessageTestCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* loginId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

