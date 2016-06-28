//
// EvhSetCardPasswordCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetCardPasswordCommand
//
@interface EvhSetCardPasswordCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* oldPassword;

@property(nonatomic, copy) NSString* theNewPassword;

@property(nonatomic, copy) NSNumber* cardId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

