//
// EvhListContactsByPhone.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactsByPhone
//
@interface EvhListContactsByPhone
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* phone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

