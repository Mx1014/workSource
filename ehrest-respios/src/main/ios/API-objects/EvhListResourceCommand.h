//
// EvhListResourceCommand.h
// generated at 2016-04-29 18:56:02 
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

