//
// EvhListResourceCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListResourceCommand
//
@interface EvhListResourceCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* userContact;

@property(nonatomic, copy) NSNumber* payerId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

