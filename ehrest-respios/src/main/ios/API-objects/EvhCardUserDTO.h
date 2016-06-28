//
// EvhCardUserDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardUserDTO
//
@interface EvhCardUserDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* cardNo;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

